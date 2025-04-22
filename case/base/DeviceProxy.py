import json
import logging

import requests

from base.Command import APIElement, Result
from base.log import logger

SERVER_HOST = "http://localhost:1234"

API_ACQUIRE_DEVICE = SERVER_HOST + "/acquire"
API_RELEASE = SERVER_HOST + "/release"
API_COMMAND = SERVER_HOST + "/command"


class DeviceProxy:
    def __init__(self, device_id, os):
        self.device_id = device_id
        self.os = os
        self.status = None

    def acquire(self):
        logger.debug(" acquire device, device_id: {}".format(self.device_id))
        response = requests.get(API_ACQUIRE_DEVICE, params={
            "device_id": self.device_id,
            "os": self.os
        })

        json = response.json()
        data = json['data']
        if len(data) == 0:
            return
        self.device_id = data['device_id']
        self.os = data['os']
        self.status = data['status']
        logger.debug(" acquire success, device_id: {}, os: {}".format(self.device_id, self.os))

    def release(self):
        logger.debug(" release device, device_id: {}".format(self.device_id))

        response = requests.get(API_RELEASE, params={
            "device_id": self.device_id,
            "os": self.os
        })

        code = response.json()['error']
        self.status = 0
        logger.debug(" release success, device_id: {}, os: {}".format(self.device_id, self.os))

    def command(self, api: APIElement):
        logger.info(f"  api call, instance={api.self_instance_key} api name={api.api_name} params={api.parameters}")

        response = requests.get(API_COMMAND, params={
            "device_id": self.device_id,
            "os": self.os,
            "data": json.dumps(api.to_json())
        })
        data = response.json()
        result = Result(code=data['code'], message=data['message'], event=data['event'], data=data['data'], device_id=data['device_id'])
        logger.info(f"  call result, ret={result.data}")
        return result

