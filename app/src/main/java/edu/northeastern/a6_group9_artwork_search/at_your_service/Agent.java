package edu.northeastern.a6_group9_artwork_search.at_your_service;

public class Agent {
    private final int id;
    private final String title;
    private final int birthDate;
    private final int deathDate;
    private final String artistDescription;

        public Agent(
                int id, String title, int birthDate, int deathDate, String artistDescription
        ) {
            this.id = id;
            this.title = title;
            this.birthDate = birthDate;
            this.deathDate = deathDate;
            this.artistDescription = artistDescription;
        }

        /**
         *
         * @return internal id of the person or organization
         */
        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        /**
         *
         * @return text to display when image load failed
         */
        public int getBirthDate() {
            return birthDate;
        }
        public int getDeathDate() {
            return deathDate;
        }
        public String getArtistDescription() {
            return artistDescription;
        }



    }

