var cal = {
  // (A) PROPERTIES
  // (A1) FLAGS & DATA
  mon : false, // monday first
  events : "", // events data for current month/year
  sMth : 0, // selected month
  sYear : 0, // selected year
  sDIM : 0, // number of days in selected month
  sF : 0, // first date of the selected month (yyyymmddhhmm)
  sL : 0, // last date of the selected month (yyyymmddhhmm)
  sFD : 0, // first day of the selected month (mon-sun)
  sLD : 0, // last day of the selected month (mon-sun)
  ready : 0, // to track loading

  // (A2) HTML ELEMENTS
  hMth : "", hYear : "", // month & year
  hCD : "", hCB : "", // calendar days & body
  hFormWrap : "", hForm : "", // event form
  hfID : "", hfStart : "", // event form fields
  hfEnd : "", hfTxt : "",
  hfColor : "", hfBG : "",
  hfDel : "",

  // (A3) INDEXED DB
  iName : "JSCalendar",
  iDB : "", iTX : "", // idb object & transaction

  // (A4) HELPER FUNCTIONS
  toDate : date => parseInt(date.replace(/-|T|:/g, "")),
  toISODate : date => {
    date = String(date);
    yr = date.slice(0,4); mth = date.slice(4,6); day = date.slice(6,8);
    hr = date.slice(8,10); min = date.slice(10);
    return `${yr}-${mth}-${day}T${hr}:${min}`;
  },

  // (B) INIT
  init : () => {
    // (B1) REQUIREMENTS CHECK - INDEXED DB
    window.indexedDB = window.indexedDB || window.mozIndexedDB || window.webkitIndexedDB || window.msIndexedDB;
    if (!window.indexedDB) {
      alert("Your browser does not support indexed database.");
      return;
    }

    // (B2) REQUIREMENTS CHECK - STORAGE CACHE
    if (!"caches" in window) {
      alert("Your browser does not support cache storage.");
      return;
    }

    // (B3) OPEN IDB
    let req = window.indexedDB.open(cal.iName, 1);

    // (B4) IDB OPEN ERROR
    req.onerror = evt => {
      alert("Indexed DB init error - " + evt.message);
      console.error(evt);
    };

    // (B5) IDB UPGRADE NEEDED
    req.onupgradeneeded = evt => {
      cal.iDB = evt.target.result;

      // (B5-1) IDB UPGRADE ERROR
      cal.iDB.onerror = evt => {
        alert("Indexed DB upgrade error - " + evt.message);
        console.error(evt);
      };

      // (B5-2) IDB VERSION 1
      if (evt.oldVersion < 1) {
        let store = cal.iDB.createObjectStore(cal.iName, {
          keyPath: "id",
          autoIncrement: true
        });
        store.createIndex("s", "s", { unique: false });
        store.createIndex("e", "e", { unique: false });
      }
    };

    // (B6) IDB OPEN OK
    req.onsuccess = evt => {
      cal.iDB = evt.target.result;
      cal.iTX = () => {
        return cal.iDB
            .transaction(cal.iName, "readwrite")
            .objectStore(cal.iName);
      };
      cal.prepare();
    };
  },

  // (C) PREPARE CALENDAR HTML INTERFACE
  prepare : () => {
    // (C1) GET HTML ELEMENTS
    cal.hMth = document.getElementById("calMonth");
    cal.hYear = document.getElementById("calYear");
    cal.hCD = document.getElementById("calDays");
    cal.hCB = document.getElementById("calBody");
    cal.hFormWrap = document.getElementById("calForm");
    cal.hForm = cal.hFormWrap.querySelector("form");
    cal.hfID = document.getElementById("evtID");
    cal.hfStart = document.getElementById("evtStart");
    cal.hfEnd = document.getElementById("evtEnd");
    cal.hfTxt = document.getElementById("evtTxt");
    cal.hfColor = document.getElementById("evtColor");
    cal.hfBG = document.getElementById("evtBG");
    cal.hfDel = document.getElementById("evtDel");
    cal.course = document.getElementById("select");
    cal.option = document.createElement("option");
    cal.events = document.getElementById("events");
    cal.schedule_id = document.getElementById("schedule_id");
    cal.myCoursesS = document.getElementById("myCourseS");


    console.log(events.value);
    cal.iTX().clear();
    var eventsArr = JSON.parse(events.value);
    for(let i=0; i<eventsArr.length;i++){
      // (I1) COLLECT DATA
      // s & e : start & end date
      // c & b : text & background color
      // t : event text
      var e={
        s : parseInt(cal.toDate(eventsArr[i].s)),
        e : parseInt(cal.toDate(eventsArr[i].e)),
        t : eventsArr[i].t,
        c : eventsArr[i].c,
        b : eventsArr[i].b,
        id:eventsArr[i].id,
        course:eventsArr[i].course
      }
      console.log(e);
      cal.iTX().put(e);
    }




    // (C2) MONTH & YEAR SELECTOR
    let now = new Date(), nowMth = now.getMonth() + 1;
    for (let [i,n] of Object.entries({
      1 : "January", 2 : "Febuary", 3 : "March", 4 : "April",
      5 : "May", 6 : "June", 7 : "July", 8 : "August",
      9 : "September", 10 : "October", 11 : "November", 12 : "December"
    })) {
      let opt = document.createElement("option");
      opt.value = i;
      opt.innerHTML = n;
      if (i==nowMth) { opt.selected = true; }
      cal.hMth.appendChild(opt);
    }
    cal.hYear.value = parseInt(now.getFullYear());

    // (C3) ATTACH CONTROLS
    cal.hMth.onchange = cal.load;
    cal.hYear.onchange = cal.load;
    document.getElementById("calBack").onclick = () => cal.pshift();
    document.getElementById("calNext").onclick = () => cal.pshift(1);
    document.getElementById("calAdd").onclick = () => cal.show();
    cal.hForm.onsubmit = () => cal.save();
    document.getElementById("evtCX").onclick = () => cal.hFormWrap.close();
    cal.hfDel.onclick = cal.del;

    // (C4) DRAW DAY NAMES
    let days = ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat"];
    if (cal.mon) { days.push("Sun"); } else { days.unshift("Sun"); }
    for (let d of days) {
      let cell = document.createElement("div");
      cell.className = "calCell";
      cell.innerHTML = d;
      cal.hCD.appendChild(cell);
    }

    // (C5) LOAD & DRAW CALENDAR
    cal.load();
  },

  // (D) SHIFT CURRENT PERIOD BY 1 MONTH
  pshift : forward => {
    cal.sMth = parseInt(cal.hMth.value);
    cal.sYear = parseInt(cal.hYear.value);
    if (forward) { cal.sMth++; } else { cal.sMth--; }
    if (cal.sMth > 12) { cal.sMth = 1; cal.sYear++; }
    if (cal.sMth < 1) { cal.sMth = 12; cal.sYear--; }
    cal.hMth.value = cal.sMth;
    cal.hYear.value = cal.sYear;
    cal.load();
  },

  // (E) LOAD EVENTS DATA FOR MONTH/YEAR
  load : () => {
    // (E1) SET SELECTED PERIOD
    cal.sMth = parseInt(cal.hMth.value);
    cal.sYear = parseInt(cal.hYear.value);
    cal.sDIM = new Date(cal.sYear, cal.sMth, 0).getDate();
    cal.sFD = new Date(cal.sYear, cal.sMth-1, 1).getDay();
    cal.sLD = new Date(cal.sYear, cal.sMth-1, cal.sDIM).getDay();
    let m = cal.sMth;
    if (m < 10) { m = "0" + m; }
    cal.sF = parseInt(String(cal.sYear) + String(m) + "010000");
    cal.sL = parseInt(String(cal.sYear) + String(m) + String(cal.sDIM) + "2359");

    // (E2) FETCH INIT
    // inefficient. but no other ways to do complex search in idb.
    cal.ready = 0;
    cal.events = {};
    let rangeA = IDBKeyRange.bound(cal.sF, cal.sL),
        rangeB = IDBKeyRange.lowerBound(cal.sL, true);

    // (E3) GET ALL START DATE THAT FALLS INSIDE MONTH/YEAR
    cal.iTX().index("s").openCursor(rangeA).onsuccess = evt => {
      let cursor = evt.target.result;
      if (cursor) {
        if (!cal.events[cursor.value.id]) {
          cal.events[cursor.value.id] = cursor.value;
        }
        cursor.continue();
      } else { cal.loading(); }
    };

    // (E4) GET ALL END DATE THAT FALLS INSIDE MONTH/YEAR
    cal.iTX().index("e").openCursor(rangeA).onsuccess = evt => {
      let cursor = evt.target.result;
      if (cursor) {
        if (!cal.events[cursor.value.id]) {
          cal.events[cursor.value.id] = cursor.value;
        }
        cursor.continue();
      } else { cal.loading(); }
    };

    // (E5) END DATE AFTER SELECTED MONTH/YEAR, BUT START IS BEFORE
    cal.iTX().index("e").openCursor(rangeB).onsuccess = evt => {
      let cursor = evt.target.result;
      if (cursor) {
        if (cursor.value.start<cal.sFirst && !cal.events[cursor.value.id]) {
          cal.events[cursor.value.id] = cursor.value;
        }
        cursor.continue();
      } else { cal.loading(); }
    };
  },

  // (F) LOADING CHECK
  loading : () => {
    cal.ready++;
    if (cal.ready==3) { cal.draw(); }
  },

  // (G) DRAW CALENDAR
  draw : () => {
    // (G1) CALCULATE DAY MONTH YEAR
    // note - jan is 0 & dec is 11 in js
    // note - sun is 0 & sat is 6 in js
    let now = new Date(), // current date
        nowMth = now.getMonth()+1, // current month
        nowYear = parseInt(now.getFullYear()), // current year
        nowDay = cal.sMth==nowMth && cal.sYear==nowYear ? now.getDate() : "" ;

    // (G2) DRAW CALENDAR ROWS & CELLS
    // (G2-1) INIT + HELPER FUNCTIONS
    let rowA, rowB, rowC, rowMap = {}, rowNum = 1,
        cell, cellNum = 1,
        rower = () => {
          rowA = document.createElement("div");
          rowB = document.createElement("div");
          rowC = document.createElement("div");
          rowA.className = "calRow";
          rowA.id = "calRow" + rowNum;
          rowB.className = "calRowHead";
          rowC.className = "calRowBack";
          cal.hCB.appendChild(rowA);
          rowA.appendChild(rowB);
          rowA.appendChild(rowC);
        },
        celler = day => {
          cell = document.createElement("div");
          cell.className = "calCell";
          if (day) { cell.innerHTML = day; }
          rowB.appendChild(cell);
          cell = document.createElement("div");
          cell.className = "calCell";
          if (day==="") { cell.classList.add("calBlank"); }
          if (day!=="" && day==nowDay) { cell.classList.add("calToday"); }
          rowC.appendChild(cell);
        };
    cal.hCB.innerHTML = ""; rower();

    // (G2-2) BLANK CELLS BEFORE START OF MONTH
    if (cal.mon && cal.sFD != 1) {
      let blanks = cal.sFD==0 ? 7 : cal.sFD ;
      for (let i=1; i<blanks; i++) { celler(); cellNum++; }
    }
    if (!cal.mon && cal.sFD != 0) {
      for (let i=0; i<cal.sFD; i++) { celler(); cellNum++; }
    }

    // (G2-3) DAYS OF THE MONTH
    for (let i=1; i<=cal.sDIM; i++) {
      rowMap[i] = { r : rowNum, c : cellNum };
      celler(i);
      if (cellNum%7==0 && i!=cal.sDIM) { rowNum++; rower(); }
      cellNum++;
    }

    // (G2-4) BLANK CELLS AFTER END OF MONTH
    if (cal.mon && cal.sLD != 0) {
      let blanks = cal.sLD==6 ? 1 : 7-cal.sLD;
      for (let i=0; i<blanks; i++) { celler(); cellNum++; }
    }
    if (!cal.mon && cal.sLD != 6) {
      let blanks = cal.sLD==0 ? 6 : 6-cal.sLD;
      for (let i=0; i<blanks; i++) { celler(); cellNum++; }
    }

    // (G3) DRAW EVENTS
    if (Object.keys(cal.events).length > 0) { for (let [id,evt] of Object.entries(cal.events)) {
      // (G3-1) EVENT START & END DAY
      let sd = new Date(cal.toISODate(evt.s)),
          ed = new Date(cal.toISODate(evt.e));
      if (sd.getFullYear() < cal.sYear) { sd = 1; }
      else { sd = sd.getMonth()+1 < cal.sMth ? 1 : sd.getDate(); }
      if (ed.getFullYear() > cal.sYear) { ed = cal.sDIM; }
      else { ed = ed.getMonth()+1 > cal.sMth ? cal.sDIM : ed.getDate(); }

      // (G3-2) "MAP" ONTO HTML CALENDAR
      cell = {}; rowNum = 0;
      for (let i=sd; i<=ed; i++) {
        if (rowNum!=rowMap[i]["r"]) {
          cell[rowMap[i]["r"]] = { s:rowMap[i]["c"], e:0 };
          rowNum = rowMap[i]["r"];
        }
        if (cell[rowNum]) { cell[rowNum]["e"] = rowMap[i]["c"]; }
      }

      // (G3-3) DRAW HTML EVENT ROW
      for (let [r,c] of Object.entries(cell)) {
        let o = c.s - 1 - ((r-1) * 7), // event cell offset
            w = c.e - c.s + 1; // event cell width
        rowA = document.getElementById("calRow"+r);
        rowB = document.createElement("div");
        rowB.className = "calRowEvt";
        rowB.innerHTML = cal.events[id]["t"];
        rowB.style.color = cal.events[id]["c"];
        rowB.style.backgroundColor  = cal.events[id]["b"];
        rowB.classList.add("w"+w);
        if (o!=0) { rowB.classList.add("o"+o); }
        rowB.onclick = () => cal.show(id);
        rowA.appendChild(rowB);
      }
    }}
  },

  // (H) SHOW EVENT FORM
  show : id => {
    if (id) {
      cal.hfID.value = id;
      cal.hfStart.value = cal.toISODate(cal.events[id]["s"]);
      cal.hfEnd.value = cal.toISODate(cal.events[id]["e"]);
      cal.hfTxt.value = cal.events[id]["t"];
      cal.hfColor.value = cal.events[id]["c"];
      cal.hfBG.value = cal.events[id]["b"];
      cal.option.value = cal.events[id]["course"];
      cal.course.add(cal.option);
      cal.option.innerHTML = cal.option.value;
      cal.option.style.display="flex";
      cal.option.selected = true;


      cal.course.disabled=true;
      cal.hfDel.style.display = "inline-block";
    } else {
      cal.hForm.reset();
      cal.hfID.value = parseInt(cal.schedule_id.value) +1;
      cal.hfDel.style.display = "none";
      cal.course.disabled=false;

      for(let i=0; i<cal.course.options.length;i++){
        console.log(cal.course.options[i].value.toString());
        console.log(cal.myCoursesS.outerText.toString());
        if(cal.myCoursesS.innerHTML.indexOf(cal.course.options[i].value) !== -1){
          cal.course.options[i].disabled=true;
        }
      }


    }
    cal.hFormWrap.show();
  },

  // (I) SAVE EVENT
  save : () => {
    // (I1) COLLECT DATA
    // s & e : start & end date
    // c & b : text & background color
    // t : event text
    var data = {
      s : cal.toDate(cal.hfStart.value),
      e : cal.toDate(cal.hfEnd.value),
      t : cal.hfTxt.value,
      c : cal.hfColor.value,
      b : cal.hfBG.value,
      course:cal.course.value
    };
    if (cal.hfID.value != "") { data.id = parseInt(cal.hfID.value); }
    console.log(data);

    // (I2) DATE CHECK
    if (new Date(data.s) > new Date(data.e)) {
      alert("Start date cannot be later than end date!");
      return false;
    }
    // CHECK Empty
    if(data.s===""||data.e===""||data.t===""||data.c===""||data.b===""||data.course===""){
      // alert("Fill in All Column");
      return false;
    }




    // (I3) SAVE
    if (data.id) { cal.iTX().put(data); }
    else { cal.iTX().add(data); }
    cal.hFormWrap.close();
    cal.load();
    return false;
  },

  // (J) DELETE EVENT
  del : () => { if (confirm("Delete Event?")) {
    cal.iTX().delete(parseInt(cal.hfID.value));
    cal.hFormWrap.close();
    cal.load();
  }}

};
window.onload = cal.init;

// function f() {
//   XMLHttpRequest
// }

