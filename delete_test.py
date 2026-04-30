import sys
import requests

model_id = "48553d05-2d95-4de2-8d76-66870a688fd1"
if len(sys.argv) > 1:
    model_id = sys.argv[1]

token_url = 'http://localhost:8080/auth/realms/openslice/protocol/openid-connect/token'
data = {'client_id': 'osapiWebClientId', 'username': 'admin', 'password': 'openslice', 'grant_type': 'password'}
token = requests.post(token_url, data=data).json()['access_token']
headers = {'Authorization': f'Bearer {token}'}
aimodel_url = f'http://localhost:13082/tmf-api/tmf-api/AiM/v4/aiModel/{model_id}'

print(f"Sending DELETE to {aimodel_url}")
resp = requests.delete(aimodel_url, headers=headers)
print('Status:', resp.status_code)
print('Response:', resp.text)
