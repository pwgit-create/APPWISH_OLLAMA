#!/bin/bash
javac -cp .:src/main/resources/external_libs/* $1 2>&1 | tee

