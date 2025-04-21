import requests

from base.Command import APIElement

SERVER_HOST = "http://localhost:1234"

API_ACQUIRE_DEVICE = SERVER_HOST + "/acquire"
API_RELEASE = SERVER_HOST + "/release"
API_COMMAND = SERVER_HOST + "/command"


class DeviceProxy:
    def __init__(self, device_id, os):
        self.device_id = device_id
        self.os = os

    def acquire(self):
        response = requests.get(API_ACQUIRE_DEVICE, params={
            "device_id": self.device_id,
            "os": self.os
        })

        data = response.json()['data']
        print(data)

    def release(self):
        response = requests.get(API_RELEASE, params={
            "device_id": self.device_id,
            "os": self.os
        })

        data = response.json()['data']
        print(data)

    def command(self, api: APIElement):
        response = requests.get(API_COMMAND, params={
            "device_id": self.device_id,
            "os": self.os,
            "api_element": api.to_json()
        })

        data = response.json()['data']
        print(data)
