package de.bdr.asset.management.user.department;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
/**
 * Implementation of Department Service
 * Currently returns only dummy data.
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {
    // TODO: Update the functions to not use dummy data
    private final DepartmentRepository repository;

    public DepartmentServiceImpl(DepartmentRepository repository) { this.repository = repository; }

    /**
     * @param departmentRequest - A DepartmentDTO record
     * @return a DepartmentDTO record
     */
    @Override
    public DepartmentRequestDTO createDepartment(DepartmentRequestDTO departmentRequest) {
        // TODO: Implement a mapper function to handle this
        
        return new DepartmentRequestDTO(
                departmentRequest.name(),
                departmentRequest.managerId()
        );
    }

    /**
     * @param id - a Long id
     * @return a DepartmentRequestDTO record
     */
    @Override
    public DepartmentRequestDTO getDepartmentById(Long id) {
        return new DepartmentRequestDTO(
                DepartmentEnum.ARCHITECTURE,
                1L
        );
    }

    /**
     * @return a List of DepartmentRequestDTO records
     */
    @Override
    public List<DepartmentRequestDTO> getAllDepartments() {
        List<DepartmentRequestDTO> dummyList = new ArrayList<>();
        dummyList.add(
                new DepartmentRequestDTO(
                        DepartmentEnum.ARCHITECTURE,
                        1L
                )
        );
        dummyList.add(
                new DepartmentRequestDTO(
                        DepartmentEnum.DEVOPS,
                        2L
                )
        );
        return dummyList;
    }

    /**
     * @param id - a Long id
     * @param departmentRequest - a DepartmentRequestDTO record
     * @return a DepartmentRequestDTO record
     */
    @Override
    public DepartmentRequestDTO updateDepartment(Long id, DepartmentRequestDTO departmentRequest) {
        return new DepartmentRequestDTO(
                DepartmentEnum.ARCHITECTURE,
                1L
        );
    }

    /**
     * @param id - a Long id
     */
    @Override
    public void deleteDepartment(Long id) {

    }
}
