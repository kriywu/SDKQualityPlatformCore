import asyncio
import json
import ssl
from asyncio import Semaphore

from aiohttp import web
from aiohttp.web_request import Request


class Device:
    def __init__(self, device_id, os):
        self.device_id = device_id  # device id
        self.os = os  # android ios else
        self.status = 0  # 0: idle, 1: busy
        self.ws = None  # websocket connection
        self.http = None  # http connection
        self.pv :Semaphore = asyncio.Semaphore(0)
        self.result = None

    def __str__(self):
        return f"Device(device_id={self.device_id}, device_os={self.os}, status={self.status})"

    def to_json(self):
        return {
            "device_id": self.device_id,
            "os": self.os,
            "status": self.status
        }


async def websocket_handler(request: Request):
    ws = web.WebSocketResponse()
    await ws.prepare(request)
    print("device connected")
    async for msg in ws:
        if msg.type != web.WSMsgType.TEXT:
            pass
        print("websocket_handler: " + msg.data)
        data = json.loads(msg.data)
        event = data['event']
        await gWebsocketHandler[event](request, ws, data)

    return ws


# WS Handler

async def register_handler(request, ws, json_data):
    device_id = json_data['data']['device_id']
    os = json_data['data']['os']
    device = Device(device_id, os)
    gDeviceList.append(device)
    device.ws = ws
    print("device_id={} register".format(device_id))
    result = json.dumps({"error": 0, "message": "success", 'event': 'register'})
    await ws.send_str(result)


async def unregister_handler(request, ws, json_data):
    device_id = json_data['data']['device_id']
    for device in gDeviceList:
        if device.device_id == device_id:
            gDeviceList.remove(device)
            print("device_id={} unregister".format(device_id))
            result = json.dumps({"error": 0, "message": "success", 'event': 'unregister'})
            await device.ws.send_str(result)
            break
    pass

async def sendCommand_handler(request, ws, json_data):
    print("sendCommand_handler " + str(json_data))
    device_id = json_data['device_id']
    event = json_data['event']
    for device in gDeviceList:
        if device.device_id == device_id and event == 'command':
            device.result = json_data
            device.pv.release()
            break
    pass



# HTTP Handler
async def acquire_handler(request: Request):
    os = request.query.get('os', None)
    result = None

    for device in gDeviceList:
        if device.status == 0 and (os is None or device.os.upper() == os.upper()):
            device.status = 1
            result = device
            break

    if result is None:
        data = []
    else:
        data = result.to_json()

    return web.Response(text=json.dumps({
        "error": 0,
        "message": "success",
        "data": data
    }))


async def command_handler(request: Request):
    device_id = request.query.get('device_id', '')
    command = request.query.get('data', '')
    targetDevice: Device = None
    for device in gDeviceList:
        if device.device_id == device_id and device.status == 1:
            targetDevice = device
            targetDevice.http = web
            break
    print("command_handler " + command)
    await targetDevice.ws.send_str(command)

    await targetDevice.pv.acquire()

    return web.Response(text=json.dumps(targetDevice.result))


async def release_handler(request: Request):
    device_id = request.query.get('device_id', '')
    for device in gDeviceList:
        if device.device_id == device_id:
            device.status = 0
            break
    return web.Response(text=json.dumps({"error": 0, "message": "success"}))


async def log_handler(request: Request):
    global gLog
    device_id = request.query.get('device_id', '')
    logs = []
    if device_id in gLog:
        gLog += logs
    else:
        gLog[device_id] = logs
    return web.Response(text=json.dumps({"error": 0, "message": "success"}))


async def hello_handler(request):
    return web.Response(text="Hello, world!")


# gDeviceList = [Device("123456", "android")]
gDeviceList = []
gWebsocketHandler = {
    'register': register_handler,
    'unregister': unregister_handler,
    'command': sendCommand_handler,
}
gLog = {}  # device_id:logs


# 启动服务
async def main():
    app = web.Application()
    app.router.add_get('/websocket', websocket_handler)  # WebSocket 路由
    app.router.add_get('/acquire', acquire_handler)
    app.router.add_get('/command', command_handler)
    app.router.add_get('/release', release_handler)
    app.router.add_get('/log', log_handler)
    app.router.add_get('/hello', hello_handler)
    runner = web.AppRunner(app)

    await runner.setup()
    await web.TCPSite(runner, '0.0.0.0', 1234).start()
    await asyncio.Future()


# 运行服务器
if __name__ == "__main__":
    asyncio.run(main())
