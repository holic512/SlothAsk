import socket
from flask import Flask
import py_eureka_client.eureka_client as eureka_client

app = Flask(__name__)

my_app_name = "service-ai"
server_host = "localhost"
server_port = 5000
eureka_server_address = "http://localhost:8761/eureka/"

# 注册服务
eureka_client.init(
    eureka_server=eureka_server_address,
    app_name=my_app_name,

    instance_host=server_host,
    instance_port=server_port,

    ha_strategy=eureka_client.HA_STRATEGY_RANDOM
)


@app.route('/get', methods=['GET'])
def hello_world():
    return 'Hello World!'


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)

