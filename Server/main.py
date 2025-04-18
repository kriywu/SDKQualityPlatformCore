import asyncio
import json
import ssl
from aiohttp import web
from aiohttp.web_request import Request


class Device:
    def __init__(self, did, os):
        self.did = did  # device id
        self.os = os  # android ios else
        self.status = 0  # 0: idle, 1: busy
        self.ws = None  # websocket connection

    def __str__(self):
        return f"Device(device_id={self.did}, device_os={self.os}, status={self.status})"

    def to_json(self):
        return {
            "did": self.did,
            "os": self.os,
            "status": self.status
        }


async def websocket_handler(request: Request):
    print("websocket_handler run")
    ws = web.WebSocketResponse()
    await ws.prepare(request)
    async for msg in ws:
        if msg.type != web.WSMsgType.TEXT:
            pass
        path = json.loads(msg.data)['path']
        gWebsocketHandler[path](request)

    return ws


# WS Handler

async def register_handler(request):
    pass


async def unregister_handler(request):
    pass


async def sendCommand_handler(request):
    pass


# HTTP Handler
async def acquire_handler(request: Request):
    os = request.query.get('os', None)
    result = None

    for device in gDeviceList:
        if device.status == 0 and (os is None or device.os == os):
            device.status = 1
            result = device
            break

    return web.Response(text=json.dumps({
        "error": 0,
        "message": "success",
        "data": result.to_json()
    }))


async def command_handler(request: Request):
    did = request.query.get('did', '')
    command = request.query.get('command', '')
    targetDevice = None
    for device in gDeviceList:
        if device.did == did and device.status == 1:
            targetDevice = device
            break
    targetDevice.ws.send_str(command)
    return web.Response(text=json.dumps({"error": 0, "message": "success"}))


async def release_handler(request: Request):
    did = request.query.get('did', '')
    for device in gDeviceList:
        if device.did == did:
            device.status = 0
            break
    return web.Response(text=json.dumps({"error": 0, "message": "success"}))


async def log_handler(request: Request):
    global gLog
    did = request.query.get('did', '')
    logs = []
    if did in gLog:
        gLog += logs
    else:
        gLog[did] = logs
    return web.Response(text=json.dumps({"error": 0, "message": "success"}))


async def hello_handler(request):
    return web.Response(text="Hello, world!")


gDeviceList = [Device("mock_did", "android")]
gWebsocketHandler = {
    'register': register_handler,
    'unregister': unregister_handler,
    'sendCommand': sendCommand_handler,
}
gLog = {}  # did:logs


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
