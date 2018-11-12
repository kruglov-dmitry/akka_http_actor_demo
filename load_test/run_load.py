from __future__ import print_function
import grequests
import random
import json
import string

URL = "http://localhost:8080/v1/auth"

class AUTHENTIFICATION_CASES:
    VALID = 1
    INVALID_STARTS_WITH_A = 2
    INVALID_DOESNT_MATCH = 3


def get_random_case():
    return random.randint(1,3)


def get_random_string(max_length=13):
    return ''.join(random.choice(string.ascii_uppercase + string.digits) for _ in range(max_length))


def generate_random_input():
    user_name = get_random_string()

    password = {
        AUTHENTIFICATION_CASES.VALID: user_name.upper(),
        AUTHENTIFICATION_CASES.INVALID_STARTS_WITH_A: "A" + get_random_string(),
        AUTHENTIFICATION_CASES.INVALID_DOESNT_MATCH: get_random_string()
    }[get_random_case()]

    return json.dumps({"username": user_name, "password": password})


if __name__ == "__main__":
    rs = (grequests.post(URL,
                         headers={'Content-Type': 'application/json'},
                         data=generate_random_input(),
                         timeout=25) for idx in xrange(1000))

    for response in grequests.map(rs):
        try:
            print(response.status_code, response.text,  response.elapsed.total_seconds())
        except Exception as e:
            print(e)
