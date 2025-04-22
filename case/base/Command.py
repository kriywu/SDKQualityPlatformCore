class Common:
    def __init__(self, device_id, os):
        self.device_id = device_id
        self.os = os

    def to_json(self):
        return {
            "device_id": self.device_id,
            "os": self.os
        }


class APIElement:
    def __init__(self, self_instance_key, api_name, parameters, return_value_key, device_id):
        self.self_instance_key = self_instance_key
        self.api_name = api_name
        self.parameters = parameters
        self.return_value_key = return_value_key
        self.event = "command"
        self.device_id = device_id

    def to_json(self):
        return {
            "clz": "MathSDK",
            "instance": self.self_instance_key,
            "api_name": self.api_name,
            "params": self.parameters,
            "return_value": self.return_value_key,
            "thread_id": "main",
            "event": self.event,
            "device_id": self.device_id,
        }


class Result:
    def __init__(self, code, message, event, data, device_id):
        self.code = code
        self.message = message
        self.event = event
        self.code = code
        self.data = data
        self.device_id = device_id

    def to_json(self):
        return {
            "code": self.code,
            "message": self.message,
            "event": self.event,
            "data": self.data,
            "device_id": self.device_id
        }


class Command:
    def __init__(self, common: Common, api_element: APIElement):
        self.common = common
        self.api_element = api_element

    def to_json(self):
        return {
            "common": self.common.to_json(),
            "data": self.api_element.to_json()
        }
