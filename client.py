from urllib2 import Request, urlopen

values = """
  {
    "login": "admin",
    "password": "admin"
  }
"""

headers = {
    'Content-Type': 'application/json'
}
request = Request('http://localhost:8080/api/session', data=values, headers=headers)

response_body = urlopen(request).read()
print response_body