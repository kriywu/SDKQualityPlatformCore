from base.Command import APIElement
from base.DeviceProxy import DeviceProxy


class MathEngine:

    def __init__(self, deviceProxy: DeviceProxy):
        self.deviceProxy = deviceProxy
        self.ins_key = "ins_key_0"

    def createEngine(self):
        api = APIElement(
            self_instance_key=None,
            api_name="createEngine",
            parameters={},
            return_value_key=None
        )
        self.deviceProxy.command(api)

    def plus(self, leftValue, rightValue):
        api = APIElement(
            self_instance_key=self.ins_key,
            api_name="plus",
            parameters={"leftValue": leftValue, "rightValue": rightValue},
            return_value_key=None
        )
        self.deviceProxy.command(api)

    def destroyEngine(self):
        api = APIElement(
            self_instance_key=self.ins_key,
            api_name="destroyEngine",
            parameters={},
            return_value_key=None
        )
        self.deviceProxy.command(api)
