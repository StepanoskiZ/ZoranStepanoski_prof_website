package com.paradox.zswebsite.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.paradox.zswebsite.IntegrationTest;
import com.paradox.zswebsite.domain.Skill;
import com.paradox.zswebsite.repository.SkillRepository;
import com.paradox.zswebsite.service.dto.SkillDTO;
import com.paradox.zswebsite.service.mapper.SkillMapper;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SkillResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SkillResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_PERCENTAGE = 0;
    private static final Integer UPDATED_PERCENTAGE = 1;

    private static final String ENTITY_API_URL = "/api/skills";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private SkillMapper skillMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSkillMockMvc;

    private Skill skill;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Skill createEntity(EntityManager em) {
        Skill skill = new Skill().name(DEFAULT_NAME).percentage(DEFAULT_PERCENTAGE);
        return skill;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Skill createUpdatedEntity(EntityManager em) {
        Skill skill = new Skill().name(UPDATED_NAME).percentage(UPDATED_PERCENTAGE);
        return skill;
    }

    @BeforeEach
    public void initTest() {
        skill = createEntity(em);
    }

    @Test
    @Transactional
    void createSkill() throws Exception {
        int databaseSizeBeforeCreate = skillRepository.findAll().size();
        // Create the Skill
        SkillDTO skillDTO = skillMapper.toDto(skill);
        restSkillMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(skillDTO)))
            .andExpect(status().isCreated());

        // Validate the Skill in the database
        List<Skill> skillList = skillRepository.findAll();
        assertThat(skillList).hasSize(databaseSizeBeforeCreate + 1);
        Skill testSkill = skillList.get(skillList.size() - 1);
        assertThat(testSkill.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSkill.getPercentage()).isEqualTo(DEFAULT_PERCENTAGE);
    }

    @Test
    @Transactional
    void createSkillWithExistingId() throws Exception {
        // Create the Skill with an existing ID
        skill.setId(1L);
        SkillDTO skillDTO = skillMapper.toDto(skill);

        int databaseSizeBeforeCreate = skillRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSkillMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(skillDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Skill in the database
        List<Skill> skillList = skillRepository.findAll();
        assertThat(skillList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = skillRepository.findAll().size();
        // set the field null
        skill.setName(null);

        // Create the Skill, which fails.
        SkillDTO skillDTO = skillMapper.toDto(skill);

        restSkillMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(skillDTO)))
            .andExpect(status().isBadRequest());

        List<Skill> skillList = skillRepository.findAll();
        assertThat(skillList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPercentageIsRequired() throws Exception {
        int databaseSizeBeforeTest = skillRepository.findAll().size();
        // set the field null
        skill.setPercentage(null);

        // Create the Skill, which fails.
        SkillDTO skillDTO = skillMapper.toDto(skill);

        restSkillMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(skillDTO)))
            .andExpect(status().isBadRequest());

        List<Skill> skillList = skillRepository.findAll();
        assertThat(skillList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSkills() throws Exception {
        // Initialize the database
        skillRepository.saveAndFlush(skill);

        // Get all the skillList
        restSkillMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(skill.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].percentage").value(hasItem(DEFAULT_PERCENTAGE)));
    }

    @Test
    @Transactional
    void getSkill() throws Exception {
        // Initialize the database
        skillRepository.saveAndFlush(skill);

        // Get the skill
        restSkillMockMvc
            .perform(get(ENTITY_API_URL_ID, skill.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(skill.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.percentage").value(DEFAULT_PERCENTAGE));
    }

    @Test
    @Transactional
    void getNonExistingSkill() throws Exception {
        // Get the skill
        restSkillMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSkill() throws Exception {
        // Initialize the database
        skillRepository.saveAndFlush(skill);

        int databaseSizeBeforeUpdate = skillRepository.findAll().size();

        // Update the skill
        Skill updatedSkill = skillRepository.findById(skill.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSkill are not directly saved in db
        em.detach(updatedSkill);
        updatedSkill.name(UPDATED_NAME).percentage(UPDATED_PERCENTAGE);
        SkillDTO skillDTO = skillMapper.toDto(updatedSkill);

        restSkillMockMvc
            .perform(
                put(ENTITY_API_URL_ID, skillDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(skillDTO))
            )
            .andExpect(status().isOk());

        // Validate the Skill in the database
        List<Skill> skillList = skillRepository.findAll();
        assertThat(skillList).hasSize(databaseSizeBeforeUpdate);
        Skill testSkill = skillList.get(skillList.size() - 1);
        assertThat(testSkill.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSkill.getPercentage()).isEqualTo(UPDATED_PERCENTAGE);
    }

    @Test
    @Transactional
    void putNonExistingSkill() throws Exception {
        int databaseSizeBeforeUpdate = skillRepository.findAll().size();
        skill.setId(longCount.incrementAndGet());

        // Create the Skill
        SkillDTO skillDTO = skillMapper.toDto(skill);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSkillMockMvc
            .perform(
                put(ENTITY_API_URL_ID, skillDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(skillDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Skill in the database
        List<Skill> skillList = skillRepository.findAll();
        assertThat(skillList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSkill() throws Exception {
        int databaseSizeBeforeUpdate = skillRepository.findAll().size();
        skill.setId(longCount.incrementAndGet());

        // Create the Skill
        SkillDTO skillDTO = skillMapper.toDto(skill);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSkillMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(skillDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Skill in the database
        List<Skill> skillList = skillRepository.findAll();
        assertThat(skillList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSkill() throws Exception {
        int databaseSizeBeforeUpdate = skillRepository.findAll().size();
        skill.setId(longCount.incrementAndGet());

        // Create the Skill
        SkillDTO skillDTO = skillMapper.toDto(skill);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSkillMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(skillDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Skill in the database
        List<Skill> skillList = skillRepository.findAll();
        assertThat(skillList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSkillWithPatch() throws Exception {
        // Initialize the database
        skillRepository.saveAndFlush(skill);

        int databaseSizeBeforeUpdate = skillRepository.findAll().size();

        // Update the skill using partial update
        Skill partialUpdatedSkill = new Skill();
        partialUpdatedSkill.setId(skill.getId());

        partialUpdatedSkill.name(UPDATED_NAME).percentage(UPDATED_PERCENTAGE);

        restSkillMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSkill.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSkill))
            )
            .andExpect(status().isOk());

        // Validate the Skill in the database
        List<Skill> skillList = skillRepository.findAll();
        assertThat(skillList).hasSize(databaseSizeBeforeUpdate);
        Skill testSkill = skillList.get(skillList.size() - 1);
        assertThat(testSkill.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSkill.getPercentage()).isEqualTo(UPDATED_PERCENTAGE);
    }

    @Test
    @Transactional
    void fullUpdateSkillWithPatch() throws Exception {
        // Initialize the database
        skillRepository.saveAndFlush(skill);

        int databaseSizeBeforeUpdate = skillRepository.findAll().size();

        // Update the skill using partial update
        Skill partialUpdatedSkill = new Skill();
        partialUpdatedSkill.setId(skill.getId());

        partialUpdatedSkill.name(UPDATED_NAME).percentage(UPDATED_PERCENTAGE);

        restSkillMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSkill.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSkill))
            )
            .andExpect(status().isOk());

        // Validate the Skill in the database
        List<Skill> skillList = skillRepository.findAll();
        assertThat(skillList).hasSize(databaseSizeBeforeUpdate);
        Skill testSkill = skillList.get(skillList.size() - 1);
        assertThat(testSkill.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSkill.getPercentage()).isEqualTo(UPDATED_PERCENTAGE);
    }

    @Test
    @Transactional
    void patchNonExistingSkill() throws Exception {
        int databaseSizeBeforeUpdate = skillRepository.findAll().size();
        skill.setId(longCount.incrementAndGet());

        // Create the Skill
        SkillDTO skillDTO = skillMapper.toDto(skill);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSkillMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, skillDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(skillDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Skill in the database
        List<Skill> skillList = skillRepository.findAll();
        assertThat(skillList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSkill() throws Exception {
        int databaseSizeBeforeUpdate = skillRepository.findAll().size();
        skill.setId(longCount.incrementAndGet());

        // Create the Skill
        SkillDTO skillDTO = skillMapper.toDto(skill);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSkillMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(skillDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Skill in the database
        List<Skill> skillList = skillRepository.findAll();
        assertThat(skillList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSkill() throws Exception {
        int databaseSizeBeforeUpdate = skillRepository.findAll().size();
        skill.setId(longCount.incrementAndGet());

        // Create the Skill
        SkillDTO skillDTO = skillMapper.toDto(skill);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSkillMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(skillDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Skill in the database
        List<Skill> skillList = skillRepository.findAll();
        assertThat(skillList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSkill() throws Exception {
        // Initialize the database
        skillRepository.saveAndFlush(skill);

        int databaseSizeBeforeDelete = skillRepository.findAll().size();

        // Delete the skill
        restSkillMockMvc
            .perform(delete(ENTITY_API_URL_ID, skill.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Skill> skillList = skillRepository.findAll();
        assertThat(skillList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
