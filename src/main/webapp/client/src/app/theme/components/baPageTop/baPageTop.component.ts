import {Component} from "@angular/core";

import {Router} from "@angular/router";

import {GlobalState} from "../../../global.state";

import {CONSTANT} from "../../../utils/constant";
import {RouteService} from "../../../service/route";
import {OrgService} from "../../../service/org";
import {AccountService} from "../../../service/account";

@Component({
  selector: 'ba-page-top',
  templateUrl: './baPageTop.html',
  styleUrls: ['./baPageTop.scss']
})
export class BaPageTop {
  public profile: any = CONSTANT.PROFILE;
  project: any = CONSTANT.CURRENT_PROJECT;
  projects: any[] = CONSTANT.RECENT_PROJECTS;
  orgId: any = CONSTANT.CURR_ORG_ID;
  orgs: any[] = CONSTANT.ALL_ORGS;

  public isScrolled: boolean = false;
  public isMenuCollapsed: boolean = false;

  constructor(private _router: Router, private _state: GlobalState, private _routeService: RouteService,
              private orgService: OrgService, private accountService: AccountService) {

    this._state.subscribe('profile.refresh', (profile) => {
      console.log('profile.refresh', profile);
      this.profile = profile;
    });

    this._state.subscribe('my.orgs.change', (data: any) => {
      console.log('my.orgs.change', data);
      if (data.currOrgId) {
        this.orgId = data.currOrgId;
      }
      if (data.orgs) {
        this.orgs = data.orgs;
      }
    });

    this._state.subscribe('recent.projects.change', (data) => {
      console.log('recent.projects.change', data);
      if (data.recentProjects) {
        this.projects = data.recentProjects;
      }
      if (data.currProject) {
        this.project = data.currProject;
      }
    });

    this.accountService.loadProfileRemote().subscribe((result: any) => {
      console.log('result', result);
      if (!result) {
        this._routeService.navTo('/login');
      }
    });

    this._state.subscribe('menu.isCollapsed', (isCollapsed) => {
      this.isMenuCollapsed = isCollapsed;
    });
  }

  public changeOrg(item: any) {
    this.orgService.setDefault(item.id, {disabled: false}).subscribe((json: any) => {
      if (json.code == 1) {
        this.orgId = item.id;

        this.accountService.changeMyOrgs(null, null);
        this.accountService.changeRecentProjects(json.recentProjects);

        this._routeService.navTo('/pages/project/list');
      }
    });
  }

  public scrolledChanged(isScrolled) {
    this.isScrolled = isScrolled;
  }

  gotoModule(module: string) {
    let url = '';
    if (module == 'design') {
      url = '/pages/design/' + CONSTANT.CURRENT_PROJECT.id + '/case';
    } else if (module == 'implement') {
      url = '/pages/implement/' + CONSTANT.CURRENT_PROJECT.id + '/plan/list';
    } else if (module == 'analysis') {
      url = '/pages/analysis/' + CONSTANT.CURRENT_PROJECT.id + '/report/list';
    }

    this._routeService.navTo(url);
  }

  logout() {
    this.accountService.logout();

    this._routeService.navTo('/pages/login');
  }
}
