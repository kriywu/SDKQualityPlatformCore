
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
    def __init__(self, self_instance_key, api_name, parameters, return_value_key):
        self.self_instance_key = self_instance_key
        self.api_name = api_name
        self.parameters = parameters
        self.return_value_key = return_value_key

    def to_json(self):
        return {
            "self_instance_key": self.self_instance_key,
            "api_name": self.api_name,
            "parameters": self.parameters,
            "return_value_key": self.return_value_key,
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
