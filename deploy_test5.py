import requests, json

token_url = 'http://localhost:8080/auth/realms/openslice/protocol/openid-connect/token'
data = {'client_id': 'osapiWebClientId', 'username': 'admin', 'password': 'openslice', 'grant_type': 'password'}
token = requests.post(token_url, data=data).json()['access_token']

headers = {'Authorization': f'Bearer {token}', 'Content-Type': 'application/json'}
aimodel_url = 'http://localhost:13082/tmf-api/tmf-api/AiM/v4/aiModel'

payload = {
    "name": "test-model-named",
    "state": "reserved",
    "software": [{"id": "dummy-software"}],
    "aiModelSpecification": {
        "id": "0414e842-790d-4b2d-89c0-3b1d4e996288",
        "name": "champion-pipeline-iris",
        "version": "1.0.0"
    },
    "serviceCharacteristic": [
        {
            "name": "platform",
            "value": "mlflow"
        }
    ]
}

resp = requests.post(aimodel_url, headers=headers, json=payload)
print('Status:', resp.status_code)
if resp.status_code in [200, 201]:
    model_id = resp.json()['id']
    print(f'Successfully created model with id {model_id}')
else:
    print('Failed to create.')
    print('Body:', resp.text)
