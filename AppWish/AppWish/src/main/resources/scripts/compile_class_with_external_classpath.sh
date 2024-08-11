#!/bin/bash
javac -cp .:external_libs/* $1 2>&1 | tee

