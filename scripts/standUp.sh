#!/usr/bin/env bash

function speed_the_database() {
    echo "Setting Global vars for $1"

    eval "docker exec -it $1 mysql -uroot -p\"pass\" -e \"SET GLOBAL max_allowed_packet = 1024*1024*14;\""
}

function create_database() {
    echo "Creating database $2 on docker container $1"
}