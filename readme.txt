------------------------
product:
int id;
string name;
productType type;
bigDecimal price;
------------------------
cart:
int id;
list<CardItem> items;
int customerId;
------------------------
CardItem:
int productId;
product product
int quantity;
------------------------
customer:
int id;
string name;
string email;
------------------------
order:
int id;
cart cart;