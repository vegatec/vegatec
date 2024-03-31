import { Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { LoginService } from 'app/login/login.service';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';

@Component({
  standalone: true,
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
  imports: [SharedModule, RouterModule],
})
export default class HomeComponent implements OnInit {
  account: Account | null = null;
  data!: any;

  constructor(
    private accountService: AccountService,
    private loginService: LoginService,
  ) {}

  ngOnInit(): void {
    this.accountService.identity().subscribe(account => (this.account = account));
    navigator.serviceWorker.addEventListener('message', (event: MessageEvent) => {
      console.log("Got message \n" + JSON.stringify(event.data));
      this.data=JSON.stringify(event.data);

    });
  }

  login(): void {
    this.loginService.login();
  }
}
