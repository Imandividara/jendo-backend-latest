# Update Doctor Images in Database

## Option 1: Use These Free Image URLs (Recommended)

Update your doctors in the database with these real image URLs:

```sql
-- Update Doctor 3 (Dr. ghghghg)
UPDATE doctors SET image_url = 'https://randomuser.me/api/portraits/men/32.jpg' WHERE id = 3;

-- Update Doctor 1 (Dr. Jane Smith)
UPDATE doctors SET image_url = 'https://randomuser.me/api/portraits/women/44.jpg' WHERE id = 1;

-- Update Doctor 2 (Dr. Jone Smith)  
UPDATE doctors SET image_url = 'https://randomuser.me/api/portraits/men/85.jpg' WHERE id = 2;
```

## Option 2: Use This Person Does Not Exist (AI-Generated Faces)

```sql
UPDATE doctors SET image_url = 'https://thispersondoesnotexist.com/' WHERE id = 1;
```

## Option 3: Use Pravatar Placeholder Service

```sql
UPDATE doctors SET image_url = 'https://i.pravatar.cc/150?img=1' WHERE id = 1;
UPDATE doctors SET image_url = 'https://i.pravatar.cc/150?img=5' WHERE id = 2;
UPDATE doctors SET image_url = 'https://i.pravatar.cc/150?img=12' WHERE id = 3;
```

## How to Run These SQL Commands

### Using PostgreSQL Command Line:
```bash
psql -U postgres -d jendo-app
# Then paste the UPDATE commands above
```

### Using PgAdmin:
1. Open PgAdmin
2. Connect to your jendo-app database
3. Open Query Tool
4. Paste and run the UPDATE commands

### Using Backend API (Create an endpoint to update):
Or I can help you create a REST API endpoint in your backend to update doctor images.

## Recommended: RandomUser.me Images

These are free, high-quality, and reliable. Just run the SQL commands above!
