#!/bin/bash
set -ex
#wget https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb
wget http://security.ubuntu.com/ubuntu/pool/universe/c/chromium-browser/chromium-browser_90.0.4430.72-0ubuntu0.16.04.1_amd64.deb
sudo apt install ./google-chrome-stable_current_amd64.deb