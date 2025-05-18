var express = require('express')
var app = express()
var fs = require('fs')
const { get } = require('http')
app.get('/', function (req, res) {
  res.send('Huck!')
})

var server = app.listen(3000, function () {
    console.log('Server is running on port 3000')
})

app.get('/getLumensSleepLog', function (req, res) {
  const id = 1;
  fs.readFile('final_lumen_dataset.json', 'utf8', function(err, data) {
    if (err) {
      res.status(500).send('Error reading file');
      return;
    }
    try {
      const jsonData = JSON.parse(data);
      const lumensLog = jsonData["SleepLog"].filter(item => item.LumenID == id);
      const lumensWakeUpLog = lumensLog.filter(item => item.SleepType == 1)
      const lumensSleepLog = lumensLog.filter(item => item.SleepType == 0)

      var sleepHours = [];
      lumensSleepLog.forEach(wu => {
        var nextDayWakeUp = lumensWakeUpLog.find(item => item.Day == wu.Day)
        
        if (nextDayWakeUp) {
          var ts1 = new Date(wu.Timestamp);
          var ts2 = new Date(nextDayWakeUp.Timestamp)
          sleepHours.push({day: wu.Day, hours: (ts2-ts1) / 3600000 });
        }
      })
      if(sleepHours.length > 7)
        sleepHours = sleepHours.slice(-7);
        
        res.json(sleepHours)
    } catch (parseErr) {
      console.error('Error parsing JSON:', parseErr);
      return;
    }
  })
})

app.use(express.json());

app.post('/getResponse/', function (req, res) {
  const question = req.body.question;
  fetch('http://10.101.11.255:5000/chat', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      "user_data": " ",
      "engine_type": 1,
      "user_input": question
    }),
  }).then(response => {
    if (!response.ok) {
      throw new Error('Network response was not ok');
    }
    return response.json();
  }).then(data => {
    console.log('Success:', data);
    res.json(data);
  }).catch(error => {
    console.error('Error:', error);
    res.status(500).send('Error fetching data from the API');
  });
})

app.get('/getEnergyLog/:id', function (req, res) {
  const id = req.params.id;
  fs.readFile('final_lumen_dataset.json', 'utf8', function(err, data) {
    if (err) {
      res.status(500).send('Error reading file');
      return;
    }
    try {
      const jsonData = JSON.parse(data);
      const energyLogMap = jsonData["EnergyLog"].filter(item => item.LumenID == id)
      if(energyLogMap.length == 0){
        res.status(404).send('ID not found');
        return;
      }
      const energyLog = energyLogMap.map(item => item.EnergyLevel)      

      const energyLogReport = {
        "LumenID": id,
        "EnergyLog" : energyLog
      }

      if(energyLogReport){
        res.json(energyLogReport);
      } else {
        res.status(404).send('Calculation error');
      }
    } catch (parseErr) {
      console.error('Error parsing JSON:', parseErr);
      return;
    }
  })
}
)

app.get('/getEnergyLocations/', function (req, res) {
  console.log("got connection")
  fs.readFile('final_lumen_dataset.json', 'utf8', function(err, data) {
    if (err) {
      res.status(500).send('Error reading file');
      return;
    }
    try {
      const jsonData = JSON.parse(data);
      const energyLocations = jsonData["EnergyLocations"]
      res.json(energyLocations);
    } catch (parseErr) {
      console.error('Error parsing JSON:', parseErr);
      return;
    }
  })
})

app.get('/generateNotification/', function (req, res) {
  console.log("got connection")
  fs.readFile('final_lumen_dataset.json', 'utf8', function(err, data) {
    if (err) {
      res.status(500).send('Error reading file');
      return;
    }
    try {
      const jsonData = JSON.parse(data);
      fetch('http://10.101.11.255:5000/chat', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          "user_data": JSON.stringify(jsonData),
          "engine_type": 0,
          "user_input": " "
          }),
      }).then(response => {
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        return response.json();
      }).then(data => {
        console.log('Success:', data);
        res.json(data);
      }).catch(error => {
        console.error('Error:', error);
        res.status(500).send('Error fetching data from the API');
      });
    } catch (parseErr) {
      console.error('Error parsing JSON:', parseErr);
      return;
    }
  })
})