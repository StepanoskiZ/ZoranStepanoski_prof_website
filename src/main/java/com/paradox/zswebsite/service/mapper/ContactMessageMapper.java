package com.paradox.zswebsite.service.mapper;

import com.paradox.zswebsite.domain.ContactMessage;
import com.paradox.zswebsite.service.dto.ContactMessageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ContactMessage} and its DTO {@link ContactMessageDTO}.
 */
@Mapper(componentModel = "spring")
public interface ContactMessageMapper extends EntityMapper<ContactMessageDTO, ContactMessage> {}
