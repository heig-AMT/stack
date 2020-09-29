#!/bin/env python3
import requests
import json

server="http://localhost:8080/questions"

def start():
    try:
        res = requests.get(server)
        status = res.status_code
    except:
        status = 500
    return status

status = 400

while (status != 200):
    status = start()
