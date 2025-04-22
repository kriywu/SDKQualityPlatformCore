from base.Command import APIElement
from base.DeviceProxy import DeviceProxy


class MathEngine:

    def __init__(self, deviceProxy: DeviceProxy):
        self.deviceProxy = deviceProxy
        self.ins_key = "ins_key_0"

    def createEngine(self):
        api = APIElement(
            self_instance_key="",
            api_name="createEngine",
            parameters={},
            return_value_key=self.ins_key,
            device_id=self.deviceProxy.device_id
        )
        self.deviceProxy.command(api)

    def plus(self, leftValue, rightValue):
        api = APIElement(
            self_instance_key=self.ins_key,
            api_name="plus",
            parameters={"leftValue": leftValue, "rightValue": rightValue},
            return_value_key="",
            device_id=self.deviceProxy.device_id
        )
        result = self.deviceProxy.command(api)
        return int(result.data)

    def destroyEngine(self):
        api = APIElement(
            self_instance_key=self.ins_key,
            api_name="destroyEngine",
            parameters={},
            return_value_key="",
            device_id=self.deviceProxy.device_id
        )
        self.deviceProxy.command(api)
