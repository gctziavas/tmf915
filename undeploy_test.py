import sys
import requests

model_id = "3388c2b9-e87a-4a40-ad5a-dcae2194fd88"
if len(sys.argv) > 1:
    model_id = sys.argv[1]

token_url = 'http://localhost:8080/auth/realms/openslice/protocol/openid-connect/token'
data = {'client_id': 'osapiWebClientId', 'username': 'admin', 'password': 'openslice', 'grant_type': 'password'}
token_resp = requests.post(token_url, data=data)
if token_resp.status_code != 200:
    print("Failed to get token", token_resp.text)
    sys.exit(1)
    
token = token_resp.json()['access_token']

headers = {'Authorization': f'Bearer {token}', 'Content-Type': 'application/json'}
aimodel_url = f'http://localhost:13082/tmf-api/tmf-api/AiM/v4/aiModel/{model_id}'

payload = {
    "state": "inactive"
}

print(f"Sending PATCH to {aimodel_url} to set state=inactive")
resp = requests.patch(aimodel_url, headers=headers, json=payload)
print('Status:', resp.status_code)
print('Response:', resp.text)
