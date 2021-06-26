import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent implements OnInit {

  backend = "http://localhost:42020"
  loginName = this.route.snapshot.paramMap.get("name")
  // @ts-ignore
  slaves: Array<any> = null

  constructor(private http: HttpClient, private route: ActivatedRoute) {
  }

  getDate(date: number): string {
    /*var d = new Date(date);
    return d.toLocaleDateString();*/

    var d = new Date(date);
    var year = d.getFullYear();
    var month = d.getMonth() + 1;
    var today = d.getDate();

    return today + "-" + month + "-" + year;
  }

  ngOnInit(): void {
    this.http.get<Array<any>>(`${this.backend}/slave`).subscribe(slaves => {
      this.slaves = slaves;
      console.log(slaves.length)
      setInterval(() => {
        this.http.get<Array<any>>(`${this.backend}/slave`).subscribe(slaves => {
          console.log(slaves.length, this.slaves.length)
          if (slaves.length != this.slaves.length) {
            this.slaves = slaves;
            const slave = slaves[slaves.length-1];
            alert(`${slave.id}, ${slave.categoryID}, ${slave.user}, ${slave.description}, ${this.getDate(slave.date)}`)
          }
        })
      }, 10000)
    })
  }

  updateSlave(auction: string, category: string, description: string): void {
    const thing = {
      id: auction,
      categoryID: category,
      user: this.loginName,
      description: description,
      date: new Date().getTime()
    }
    this.http.put(`${this.backend}/slave?name=${this.loginName}`, thing).subscribe(thing => {

    })
  }

  addSlave(category: string, description: string): void {
    this.http.post(`${this.backend}/slave?categoryName=${category}`, {
      id: 0,
      categoryID: 0,
      user: this.loginName,
      description: description,
      date: new Date().getTime()
    }).subscribe(thing => {
      this.slaves.push({
        id: 0,
        categoryID: 0,
        user: this.loginName,
        description: description,
        date: new Date().getTime()
      })
    })
  }
}
