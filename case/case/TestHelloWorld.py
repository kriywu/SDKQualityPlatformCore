from base.BaseTestCase import BaseTestCase
from base.DeviceProxy import DeviceProxy
from base.MathEngine import MathEngine


class TestHelloWorld(BaseTestCase):

    def run(self):
        # 占用设备
        device = DeviceProxy(device_id="20250418001", os='android')
        device.acquire()

        mathEngine = MathEngine(device)
        mathEngine.createEngine()
        result = mathEngine.plus(leftValue=1, rightValue=1)
        mathEngine.destroyEngine()

        device.release()

        assert result == 2


if __name__ == '__main__':
    TestHelloWorld().run()