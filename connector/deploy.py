# coding:utf-8


import json
import os


def loadSettings():
    with open('boot.json', 'r') as f:
        settings = json.load(f)
        path = os.path.abspath(".") + "/target"
        jar = ""
        for i in os.listdir(path):
            if os.path.splitext(i)[1] == ".jar":
                jar = i
                break
        xmx = settings["dev"]["Xmx"]
        xms = settings["dev"]["Xms"]
        # xmn = settings["dev"]["Xmn"]
        cmd = "java -jar "+"-Xmx"+xmx+" -Xms"+xms +" target/"+jar + " >/dev/null 2>&1 &"
        return cmd


if __name__ == "__main__":
    os.system("git pull")
    os.system("mvn clean package -U -D skipTests")
    cmd = loadSettings()
    print("cmd: "+ cmd)
    os.system(cmd)
