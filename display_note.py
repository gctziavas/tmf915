import requests, json

token_url = 'http://localhost:8080/auth/realms/openslice/protocol/openid-connect/token'
data = {'client_id': 'osapiWebClientId', 'username': 'admin', 'password': 'openslice', 'grant_type': 'password'}
token = requests.post(token_url, data=data).json()['access_token']
headers = {'Authorization': f'Bearer {token}'}

# Using the model ID from our last test run
model_id = "b7aa0bbc-114e-45df-835c-ea8087d30cd2"
aimodel_url = f'http://localhost:13082/tmf-api/tmf-api/AiM/v4/aiModel/{model_id}'

resp = requests.get(aimodel_url, headers=headers)
model_data = resp.json()

print(json.dumps(model_data.get('note', []), indent=2))
