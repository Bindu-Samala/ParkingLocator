import { ParkingSpot } from './parking-spot.model';
import { User } from './user.model';

export class Booking {
  id: number;
  bookingDate: Date;
  bookingStartTime: Date;
  bookingEndTime: Date;
  parkingSpot: ParkingSpot;
  user: User;

  constructor(id: number, bookingDate: Date, bookingStartTime: Date, bookingEndTime: Date, parkingSpot: ParkingSpot, user: User) {
    this.id = id;
    this.bookingDate = bookingDate;
    this.bookingStartTime = bookingStartTime;
    this.bookingEndTime = bookingEndTime;
    this.parkingSpot = parkingSpot;
    this.user = user;
  }
}
