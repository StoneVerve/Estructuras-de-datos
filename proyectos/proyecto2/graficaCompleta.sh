#!/bin/bash
#Este script genera archivos correspondientes a las gráficas completas,
#funciona como demo para generar archivos de gráficas para proyecto2.
#El primer parámetro de entrada define k para la gráfica k-completa.

if [  $1 = "-h" ] || [  $1 = "--help" ]; then
	echo "graficasregulares.sh ENTERO"
	echo "    Para la gráfica ENTERO-completa"
else
	COUNTER=1
	COUNTER2=2

	echo "#Grafica"
	while [  $COUNTER -le $1  ]; do
		printf "%s," "$COUNTER"
		let COUNTER=COUNTER+1
	done

	echo  

	let COUNTER=1

	while [  $COUNTER -le $1  ]; do
		while [  $COUNTER2 -le $1  ]; do
			if [  $COUNTER -ne $COUNTER2  ]; then
				printf "%s,%s; " "$COUNTER" "$COUNTER2"
			fi
			let COUNTER2=COUNTER2+1
			done
		let COUNTER=COUNTER+1 
		let COUNTER2=COUNTER
	done
fi