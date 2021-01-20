import { Component, OnInit } from '@angular/core';
import { BusinessService } from '../business.service';
import { Business } from '../business';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-update-business',
  templateUrl: './update-business.component.html',
  styleUrls: ['./update-business.component.css']
})
export class UpdateBusinessComponent implements OnInit {

  id: string;
  business: Business = new Business();
  constructor(private businessService: BusinessService,
    private route: ActivatedRoute,
    private router: Router) { }

  ngOnInit(): void {
    this.id = this.route.snapshot.params['id'];

    this.businessService.getBusinessById(this.id).subscribe(data => {
      this.business = data;
    }, error => console.log(error));
  }

  onSubmit(){
    this.businessService.updateBusiness(this.id, this.business).subscribe( data =>{
      this.goToBusinessList();
    }
    , error => console.log(error));
  }

  goToBusinessList(){
    this.router.navigate(['/businesses']);
  }
}
