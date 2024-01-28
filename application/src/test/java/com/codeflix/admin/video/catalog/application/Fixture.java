package com.codeflix.admin.video.catalog.application;

import com.codeflix.admin.video.catalog.domain.castmember.CastMember;
import com.codeflix.admin.video.catalog.domain.castmember.CastMemberType;
import com.codeflix.admin.video.catalog.domain.category.Category;
import com.codeflix.admin.video.catalog.domain.genre.Genre;
import com.github.javafaker.Faker;

public final class Fixture {

    private static final Faker FAKER = new Faker();

    public static String name() {
        return FAKER.name().fullName();
    }

    public static Integer year() {
        return FAKER.random().nextInt(2020, 2030);
    }

    public static Double duration() {
        return FAKER.options().option(120.0, 15.5, 35.5, 10.0, 2.0);
    }

    public static boolean bool() {
        return FAKER.bool().bool();
    }

    public static String title() {
        return FAKER.options().option(
                "Mussum Ipsum, cacilds vidis litro abertis",
                "Aenean aliquam molestie leo, vitae iaculis nisl",
                "Detraxit consequat et quo num tendi nada"
        );
    }

    public static final class Categories {

        private static final Category CLASSES =
                Category.newCategory("Classes", "Some description", true);

        private static final Category LIVES =
                Category.newCategory("Lives", "Some description", true);

        public static Category classes() throws CloneNotSupportedException {
            return Category.from(CLASSES);
        }

        public static Category lives() {
            return Category.from(LIVES);
        }
    }

    public static final class CastMembers {

        private static final CastMember WESLEY =
                CastMember.newMember("Wesley FullCycle", CastMemberType.ACTOR);

        private static final CastMember GABRIEL =
                CastMember.newMember("Gabriel FullCycle", CastMemberType.ACTOR);

        public static CastMemberType type() {
            return FAKER.options().option(CastMemberType.values());
        }

        public static CastMember wesley() {
            return CastMember.from(WESLEY);
        }

        public static CastMember gabriel() {
            return CastMember.from(GABRIEL);
        }
    }

    public static final class Genres {

        private static final Genre TECH =
                Genre.newGenre("Technology", true);

        private static final Genre BUSINESS =
                Genre.newGenre("Business", true);

        public static Genre tech() {
            return Genre.from(TECH);
        }

        public static Genre business() {
            return Genre.from(BUSINESS);
        }
    }
}
