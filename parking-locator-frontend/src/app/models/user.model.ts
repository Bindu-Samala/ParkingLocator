// src/app/models/user.model.ts
import { Role } from '../models/role.model';

export interface User {
  id?: number;
  username: string;
  password:string,
  email: string;
  roles?: Role[];  // A user can have multiple roles (many-to-many relationship)
}
