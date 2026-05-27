package de.bdr.asset.management.department;

import de.bdr.asset.management.user.department.*;
import de.bdr.asset.management.user.department.dtos.DepartmentRequestDTO;
import de.bdr.asset.management.user.department.dtos.DepartmentResponseDTO;
import de.bdr.asset.management.user.department.dtos.DepartmentUpdateRequestDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DepartmentControllerTest {
    @Mock
    private DepartmentService departmentService;

    @InjectMocks
    private DepartmentController departmentController;

    /** CREATE */
    @Test
    void createDepartment_validRequest_returnsCreatedStatus(){
        DepartmentRequestDTO request=new DepartmentRequestDTO( DepartmentEnum.DEVOPS, 2L);
        DepartmentResponseDTO response=new DepartmentResponseDTO(1L,  DepartmentEnum.DEVOPS, 2L);

        when(departmentService.createDepartment(request)).thenReturn(response);

        ResponseEntity<DepartmentResponseDTO> result = departmentController.create(request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result.getBody()).isEqualTo(response);
        verify(departmentService).createDepartment(request);

    }

    /** READ ALL */
    @Test
    void getAllDepartments_returnsOkWithList(){
        DepartmentResponseDTO response = 
            new DepartmentResponseDTO(
                1L, 
                DepartmentEnum.DEVOPS,
                2L
            );

        List<DepartmentResponseDTO> list = List.of(response);
        Page<DepartmentResponseDTO> page = new PageImpl<>(list);

        when(departmentService.getAllDepartments(any(Pageable.class)))
            .thenReturn(page);

        ResponseEntity<Page<DepartmentResponseDTO>> result =
            departmentController.getAll(PageRequest.of(0, 10));

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assert(result.getBody() != null);
        assertThat(result.getBody().getContent())
            .hasSize(1)
            .contains(response);
    }

    /** READ BY ID */
    @Test
    void getDepartmentById_returnsOkWithDepartment(){
        DepartmentResponseDTO response=new DepartmentResponseDTO(1L,  DepartmentEnum.DEVOPS, 2L);

        when(departmentService.getDepartmentById(1L)).thenReturn(response);

        ResponseEntity<DepartmentResponseDTO> result = departmentController.getById(1L);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(response);
    }

    /** UPDATE */
    @Test
    void updateDepartment_returnsOkWithUpdatesDepartment(){
        DepartmentUpdateRequestDTO request = new DepartmentUpdateRequestDTO(DepartmentEnum.DEVOPS, 2L);
        DepartmentResponseDTO response = new DepartmentResponseDTO(1L, DepartmentEnum.DEVOPS, 2L);

        when(departmentService.updateDepartment(1L, request)).thenReturn(response);

        ResponseEntity<DepartmentResponseDTO> result = departmentController.update(1L, request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(response);
    }
}
