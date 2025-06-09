from sqlalchemy import create_engine
from database_design import Base  # Adjust this import if your file path is different

# Database connection URL
DATABASE_URL = "postgresql+psycopg2://postgres:projectmgnt@localhost/project_management"

# Create engine
engine = create_engine(DATABASE_URL)

# Drop all tables
print("Dropping all tables...")
Base.metadata.drop_all(engine)
print("All tables dropped successfully.")
