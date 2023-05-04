# REST API [JSON]
Description of rest api endpoints, exmaples

### Adding new city values
```
http://<ip-address>:8080/api/addTown?town=Paris&country=FR&state=Ile-de-France&latitude=48.8588897&longitude=2.3200410217200766
```

### Adding new measurement values
```
http://<ip-address>:8080/api/addMeasurement?town=Paris&weather=Clear&description=clear sky&temperature=288.45&humidity=63&pressure=1019&windSpeed=4.63&windDegree=70&clouds=0&dateTime=2023-05-04T11:27:47.639464840
```

### Editing city values
```
http://<ip-address>:8080/api/editTown?town=Paris&state=upravenahodnota&longitude=22.3200410217200766
```

### Editing measurement values
```
http://<ip-address>:8080/api/editMeasurement?town=Paris&temperature=555.55&dateTime=2023-05-04T11:27:47.639464840
```

### Deleting city values
```
http://<ip-address>:8080/api/deleteTown?town=Paris
```

### Deleting measurement values
```
http://<ip-address>:8080/api/deleteMeasurement?town=Paris&dateTime=2023-05-04T11:27:47.639464840
```

### List city values
```
http://<ip-address>:8080/api/listTown
```

### List measurement values
```
http://<ip-address>:8080/api/listMeasurement
```

### Stats measurement values, daily, weekly, 14 days
```
http://<ip-address>:8080/api/statsMeasurement
```

# REST API [WEB]
These points provide additional functionality but a web page is recommended to facilitate parameter entry

### Removing expired values [post params]
```
http://<ip-address>:8080/remove-expired
```

### Download measurement from open weather api [post params]
```
http://<ip-address>:8080/download
```

### Import measurement csv file [post params]
```
http://<ip-address>:8080/import
```
### Download measurement csv file [post params]
```
http://<ip-address>:8080/export
```