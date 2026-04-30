import requests, json, time

# 1. Get token
token_url = 'http://localhost:8080/auth/realms/openslice/protocol/openid-connect/token'
data = {'client_id': 'osapiWebClientId', 'username': 'admin', 'password': 'openslice', 'grant_type': 'password'}
token = requests.post(token_url, data=data).json()['access_token']
headers = {'Authorization': f'Bearer {token}', 'Content-Type': 'application/json'}

aimodel_url = 'http://localhost:13082/tmf-api/tmf-api/AiM/v4/aiModel'
aimodel_spec_url = 'http://localhost:13082/tmf-api/tmf-api/AiM/v4/aiModelSpecification'

def create_and_deploy_model(name, spec_id, target_state="reserved"):
    payload = {
        "name": name,
        "state": target_state,
        "software": [{"id": "dummy-software"}],
        "aiModelSpecification": {
            "id": spec_id
        },
        "serviceCharacteristic": [
            {
                "name": "platform",
                "value": "mlflow"
            }
        ]
    }
    resp = requests.post(aimodel_url, headers=headers, json=payload)
    if resp.status_code in [200, 201]:
        return resp.json()['id']
    else:
        print('Failed to create model:', resp.text)
        return None

def check_model_state(model_id):
    resp = requests.get(f"{aimodel_url}/{model_id}", headers=headers)
    return resp.json()

def check_spec_state(spec_id):
    resp = requests.get(f"{aimodel_spec_url}/{spec_id}", headers=headers)
    return resp.json()

# Scenario using spec UUID 00736084-286d-44c7-940f-2ef419a06bf4
print("--- SCENARIO: DEPLOYING SPECIFIC SPEC ---")
spec_id = "00736084-286d-44c7-940f-2ef419a06bf4"
print(f"Using Spec: {spec_id}")

model_id = create_and_deploy_model("specific-spec-test", spec_id)
if model_id:
    print(f"Created model {model_id}. Waiting 5 seconds for background deploy...")
    time.sleep(5)
    model_data = check_model_state(model_id)
    print("State after failure/success:", model_data.get('state'))
    print("Notes directly on model:", json.dumps(model_data.get('note', []), indent=2))
    
    spec_data = check_spec_state(spec_id)
    
    chars = spec_data.get('specCharacteristic', [])
    char_flag = next((c for c in chars if c.get('name') == 'model_file_exists'), None)
    print("Model file exists Characteristic on spec:", json.dumps(char_flag, indent=2))

