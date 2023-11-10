-- Create Block table
CREATE TABLE block (
                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       start_date DATE NOT NULL,
                       end_date DATE NOT NULL,
                       reason VARCHAR(255) NOT NULL
);

-- Create Booking table with additional status and cancellation date
CREATE TABLE booking (
                         id BIGINT PRIMARY KEY AUTO_INCREMENT,
                         start_date DATE NOT NULL,
                         end_date DATE NOT NULL,
                         guest_data VARCHAR(255) NOT NULL,
                         status VARCHAR(50) NOT NULL,
                         cancellation_date DATE
);

-- Set default values for the status column in the Booking table
ALTER TABLE booking ALTER COLUMN status SET DEFAULT 'ACTIVE';

-- Insert initial data into Block table
INSERT INTO block (start_date, end_date, reason) VALUES
                                                     ('2023-01-01', '2023-01-10', 'Maintenance'),
                                                     ('2023-01-15', '2023-01-20', 'Private Event'),
                                                     ('2023-02-05', '2023-02-10', 'Renovation'),
                                                     ('2023-03-01', '2023-03-05', 'Conference'),
                                                     ('2023-04-10', '2023-04-15', 'Scheduled Closure');

-- Insert initial data into Booking table
INSERT INTO booking (start_date, end_date, guest_data, status) VALUES
                                                                   ('2023-01-11', '2023-01-14', 'Guest A', 'ACTIVE'),
                                                                   ('2023-01-21', '2023-01-30', 'Guest B', 'ACTIVE'),
                                                                   ('2023-02-11', '2023-02-28', 'Guest C', 'ACTIVE'),
                                                                   ('2023-03-06', '2023-03-09', 'Guest D', 'ACTIVE'),
                                                                   ('2023-04-01', '2023-04-09', 'Guest E', 'ACTIVE');

