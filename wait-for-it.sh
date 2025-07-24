#!/usr/bin/env bash

host="$1"
shift
cmd="$@"

echo "Esperando a que $host esté disponible..."

already_waiting=false

until nc -z ${host%:*} ${host##*:}; do
  if [ "$already_waiting" = false ]; then
    >&2 echo "MySQL no disponible aún - esperando..."
    already_waiting=true
  fi
  sleep 2
done

>&2 echo "MySQL está arriba - ejecutando la app"
exec $cmd
