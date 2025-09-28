package com.paradox.zswebsite.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.paradox.zswebsite.domain.Authority} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AuthorityDTO implements Serializable {

    private String name; // <-- CORRECTED: Field is 'name', not 'id'

    public String getName() { // <-- CORRECTED: Getter is 'getName'
        return name;
    }

    public void setName(String name) { // <-- CORRECTED: Setter is 'setName'
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AuthorityDTO)) {
            return false;
        }

        AuthorityDTO authorityDTO = (AuthorityDTO) o;
        if (this.name == null) { // <-- CORRECTED: Check 'name'
            return false;
        }
        return Objects.equals(this.name, authorityDTO.name); // <-- CORRECTED: Compare 'name'
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name); // <-- CORRECTED: Hash 'name'
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AuthorityDTO{" +
            "name='" + getName() + "'" + // <-- CORRECTED: Use 'name'
            "}";
    }
}
