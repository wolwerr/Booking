package com.example.host.EndToEndBlockTest;

import com.example.host.entities.Block;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.net.URI;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BlockE2ETest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testCreateAndGetBlock() throws Exception {
        // Create a Block
        final String baseUrl = "http://localhost:" + port + "/api/v1/blocks";
        URI uri = new URI(baseUrl);

        Map<String, Object> block = new HashMap<>();
        block.put("startDate", LocalDate.now().toString());
        block.put("endDate", LocalDate.now().plusDays(5).toString());
        block.put("reason", "E2E Test Block");

        ResponseEntity<Block> result = restTemplate.postForEntity(uri, block, Block.class);

        // Verify if the block was created
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Block createdBlock = result.getBody();
        assertThat(createdBlock).isNotNull();
        assertThat(createdBlock.getReason()).isEqualTo("E2E Test Block");

        // Try to retrieve the Block
        Block retrievedBlock = restTemplate.getForObject(baseUrl + "/" + createdBlock.getId(), Block.class);

        // Verify if the block was retrieved is equal to the created one
        assertThat(retrievedBlock).isNotNull();
        assertThat(retrievedBlock.getId()).isEqualTo(createdBlock.getId());
        assertThat(retrievedBlock.getReason()).isEqualTo(createdBlock.getReason());
    }

    @Test
    void testBlockLifecycle() throws Exception {
        // Create URI
        final String baseUrl = "http://localhost:" + port + "/api/v1/blocks";
        URI uri = new URI(baseUrl);

        // Create a Block
        Map<String, Object> block = new HashMap<>();
        block.put("startDate", LocalDate.now().toString());
        block.put("endDate", LocalDate.now().plusDays(5).toString());
        block.put("reason", "Teste de Block");

        ResponseEntity<Block> createResponse = restTemplate.postForEntity(uri, block, Block.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Block createdBlock = createResponse.getBody();
        assert createdBlock != null;
        assertThat(createdBlock.getReason()).isEqualTo("Teste de Block");

        // Retrieve the Blocks list
        ResponseEntity<List> listResponse = restTemplate.getForEntity(uri, List.class);
        assertThat(listResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(listResponse.getBody()).hasSize(6);

        // Update the Block
        createdBlock.setReason("Updated Maintenance");
        restTemplate.put(uri + "/" + createdBlock.getId(), createdBlock);
        Block updatedBlock = restTemplate.getForObject(uri + "/" + createdBlock.getId(), Block.class);
        assertThat(updatedBlock.getReason()).isEqualTo("Updated Maintenance");

        // Delete the Block
        restTemplate.delete(uri + "/" + createdBlock.getId());
        ResponseEntity<Block> getResponse = restTemplate.getForEntity(uri + "/" + createdBlock.getId(), Block.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}

