export class ParkingSpot {
    id: number;
    location: string;
    spotNumber: string;
    isBooked: boolean;
    latitude: number;
    longitude: number;
    bookedBy: string | null; // Assuming the user's name will be assigned when it's booked
  
    constructor(id: number, location: string, spotNumber: string, isBooked: boolean, latitude: number, longitude: number, bookedBy: string | null) {
      this.id = id;
      this.location = location;
      this.spotNumber = spotNumber;
      this.isBooked = isBooked;
      this.latitude = latitude;
      this.longitude = longitude;
      this.bookedBy = bookedBy;
    }
  }
  