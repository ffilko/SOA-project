export interface OrderItem {
  id?: string;
  tourId: string;
  tourName: string;
  price: number;
}

export interface ShoppingCart {
  id: string;
  touristId: string;
  totalPrice: number;
  items: OrderItem[];
}