package de.bdr.asset.management.user.department;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
    public DepartmentDTO createDepartment(DepartmentDTO departmentRequest) {
        return new DepartmentDTO(
                1L,
                DepartmentEnum.ARCHITECTURE,
                1L
        );
    }

    /**
     * @param id - a Long id
     * @return a DepartmentDTO record
     */
    @Override
    public DepartmentDTO getDepartmentById(Long id) {
        return new DepartmentDTO(
                1L,
                DepartmentEnum.ARCHITECTURE,
                1L
        );
    }

    /**
     * @return a List of DepartmentDTO records
     */
    @Override
    public List<DepartmentDTO> getAllDepartments() {
        List<DepartmentDTO> dummyList = new ArrayList<>();
        dummyList.add(
                new DepartmentDTO(
                        1L,
                        DepartmentEnum.ARCHITECTURE,
                        1L
                )
        );
        dummyList.add(
                new DepartmentDTO(
                        2L,
                        DepartmentEnum.DEVOPS,
                        2L
                )
        );
        return dummyList;
    }

    /**
     * @param id - a Long id
     * @param departmentRequest - a DepartmentDTO record
     * @return a DepartmentDTO record
     */
    @Override
    public DepartmentDTO updateDepartment(Long id, DepartmentDTO departmentRequest) {
        return new DepartmentDTO(
                1L,
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
