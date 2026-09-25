# jenkins-mcp-demo
```
GitHub Codespace
      │
      ▼
VS Code + GitHub Copilot
      │
      │ MCP / Streamable HTTP
      ▼
Jenkins MCP Server
      │
      ▼
Jenkins
├── Applications
├── Infrastructure
├── DevOps
└── Data
```
## Running Jenkins with docker
```
mkdir -p jenkins_home
docker compose up -d
```

The compose setup builds Jenkins with the plugins listed in `plugins.txt` and
uses [casc.yml](./casc.yml) to create the initial local administrator as
`admin` / `admin`.

## Installed required plugins
See [plugins.txt](./plugins.txt) for the complete plugin list.


## Create dummy jobs
1. Create seed job (freestyle) with  (jobdsl script)[./seed_job.groovy] 
2. Run the seed job.


## Register mcp server 
I am using copilot as example. You can set in any other AI tool that supports mcp server (refer https://plugins.jenkins.io/mcp-server/)

Assuming username and password is admin/admin for jenkins you can run below command to register mcp server in copilot
```
export JENKINS_AUTH=$(printf '%s' "admin:admin" | base64 -w 0)
copilot mcp add --transport http   --header "Authorization: Basic $JENKINS_AUTH"   jenkins   http://localhost:8080/mcp-server/mcp

## see if the mcp server is listed
copilot mcp list
```

alertnativly, you can create .vscode/mcp.json file with below content
```
{
  "servers": {
    "jenkins": {
      "type": "http",
      "url": "http://localhost:8080/mcp-server/mcp",
      "headers": {
        "Authorization": "Basic YWRtaW46YWRtaW4="
      }
    }
  }
}
```