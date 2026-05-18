import { Component, OnInit } from '@angular/core';
import { KeyPoint } from 'src/app/models/keyPoint';
import { Tour, TourDifficulty } from 'src/app/models/tour';
import { AuthService } from 'src/app/services/auth.service';
import { TourService } from 'src/app/services/tour.service';


@Component({
  selector: 'app-tours',
  templateUrl: './tours.component.html',
  styleUrls: ['./tours.component.css']
})
export class ToursComponent implements OnInit {

  tours: Tour[] = [];
  tagsInput: string = '';
  newTour: Tour = {
    id: '',
    authorId: '',
    name: '',
    description: '',
    difficulty: TourDifficulty.EASY,
    tags: []
  };
  
  selectedTour: Tour | null = null;
  newKeyPoint: KeyPoint = {
    id: '',
    name: '',
    description: '',
    latitude: 0,
    longitude: 0,
    imageUrl: ''
  };

  constructor(
    private tourService: TourService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.loadTours();
  }

  loadTours(): void {
    const authorId = this.authService.getUserId();
    if (!authorId) {
      console.log('User is not logged in');
    return;
    }
    //const authorId = "123";

    this.tourService.getToursByAuthor(authorId).subscribe({
      next: (data: Tour[]) => {
        this.tours = data;
      },
      error: (err: any) => {
        console.log(err);
      }
    });
  }

  createTour(): void {

    const authorId = this.authService.getUserId();
    if (!authorId) {
      console.log('User is not logged in');
    return;
    }

    this.newTour.authorId = authorId;

    this.newTour.tags = this.tagsInput
      .split(',')
      .map(tag => tag.trim());

    this.tourService.addTour(this.newTour).subscribe({
      next: (createdTour: any) => {

        this.tours.push(createdTour);

        this.newTour = {
          id: '',
          authorId: '',
          name: '',
          description: '',
          difficulty: TourDifficulty.EASY,
          tags: []
        };

        this.tagsInput = '';
      },

      error: (err: any) => {
        console.log(err);
      }
    });
  }

  openKeyPointModal(tour: Tour): void {
    this.selectedTour = tour;
    }

  closeKeyPointModal(): void {

    this.selectedTour = null;

    this.newKeyPoint = {
      id: '',
      name: '',
      description: '',
      latitude: 0,
      longitude: 0,
      imageUrl: ''
    };
  }

  saveKeyPoint(): void {

  if (!this.selectedTour) return;

  this.tourService
    .addKeyPoint(this.selectedTour.id, this.newKeyPoint)
    .subscribe({

      next: (updatedTour: Tour) => {

        const index = this.tours.findIndex(
          t => t.id === updatedTour.id
        );

        if (index !== -1) {
          this.tours[index] = updatedTour;
        }

        this.closeKeyPointModal();
      },

      error: (err: any) => {
        console.log(err);
      }
    });
  }

}