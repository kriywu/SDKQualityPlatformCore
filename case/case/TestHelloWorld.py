from base.BaseTestCase import BaseTestCase
from base.DeviceProxy import DeviceProxy
from base.MathEngine import MathEngine


class TestHelloWorld(BaseTestCase):

    def run(self):
        # 占用设备
        device = DeviceProxy("1234567", 'android')
        device.acquire()

        mathEngine = MathEngine(device)
        mathEngine.createEngine()
        result = mathEngine.plus(leftValue=1, rightValue=1)
        mathEngine.destroyEngine()

        device.release()

        assert result == 2
