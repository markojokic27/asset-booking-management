/* unit tests for user CSV export */

import { exportUsersCsv } from '../../features/user/utilis/csv';

/* base user for testing */
const baseUser = {
  id: 1,
  name: 'Ivan',
  surname: 'Ivić',
  email: 'ivan@example.com',
  username: 'ivanivic',
  role: 'EMPLOYEE',
  status: 'ACTIVE',
  departmentId: 5,
  managerEmail: 'manager@example.com',
  notes: '',
};

const CSV_HEADERS =
  'id,name,surname,email,username,role,status,departmentId,managerEmail,notes';

/* describe exportUsersCsv function */
describe('exportUsersCsv', () => {
  let capturedBlob: Blob | null;
  let capturedBlobContent: string | null;
  let mockAnchor: HTMLAnchorElement;
  let createObjectURL: jest.Mock;
  let revokeObjectURL: jest.Mock;
  const OriginalBlob = globalThis.Blob;

  beforeEach(() => {
    capturedBlob = null;
    capturedBlobContent = null;
    mockAnchor = {
      href: '',
      download: '',
      click: jest.fn(),
    } as unknown as HTMLAnchorElement;

    jest.spyOn(Date.prototype, 'toISOString').mockReturnValue('2025-05-22T12:00:00.000Z');

    globalThis.Blob = jest.fn((parts: BlobPart[]) => {
      capturedBlobContent = parts.map(String).join('');
      capturedBlob = new OriginalBlob(parts);
      return capturedBlob;
    }) as unknown as typeof Blob;

    createObjectURL = jest.fn((blob: Blob) => {
      capturedBlob = blob;
      return 'blob:mock-url';
    });
    revokeObjectURL = jest.fn();
    globalThis.URL.createObjectURL = createObjectURL;
    globalThis.URL.revokeObjectURL = revokeObjectURL;

    jest.spyOn(document, 'createElement').mockReturnValue(mockAnchor);
  });

  afterEach(() => {
    globalThis.Blob = OriginalBlob;
    jest.restoreAllMocks();
  });

  function exportAndReadCsv(users: Record<string, unknown>[]) {
    exportUsersCsv(users);
    expect(capturedBlobContent).not.toBeNull();
    return capturedBlobContent!.replace(/^\uFEFF/, '');
  }

  it('prepends UTF-8 BOM to the blob', () => {
    exportUsersCsv([baseUser]);
    expect(capturedBlobContent?.startsWith('\uFEFF')).toBe(true);
  });

  it('builds header row and one data row', () => {
    const csv = exportAndReadCsv([baseUser]);
    const lines = csv.split('\r\n');

    expect(lines).toHaveLength(2);
    expect(lines[0]).toBe(CSV_HEADERS);
    expect(lines[1]).toBe(
      '1,Ivan,Ivić,ivan@example.com,ivanivic,EMPLOYEE,ACTIVE,5,manager@example.com,',
    );
  });

  it('builds multiple data rows', () => {
    const csv = exportAndReadCsv([
      baseUser,
      { ...baseUser, id: 2, name: 'Ana', username: 'anaanic' },
    ]);
    const lines = csv.split('\r\n');

    expect(lines).toHaveLength(3);
    expect(lines[2]).toBe(
      '2,Ana,Ivić,ivan@example.com,anaanic,EMPLOYEE,ACTIVE,5,manager@example.com,',
    );
  });

  it('quotes and escapes values containing commas', () => {
    const csv = exportAndReadCsv([{ ...baseUser, notes: 'a,b' }]);
    expect(csv).toContain('"a,b"');
  });

  it('quotes and doubles internal double quotes', () => {
    const csv = exportAndReadCsv([{ ...baseUser, notes: 'say "hi"' }]);
    expect(csv).toContain('"say ""hi"""');
  });

  it('quotes values containing newlines', () => {
    const csv = exportAndReadCsv([{ ...baseUser, notes: 'line1\nline2' }]);
    expect(csv).toContain('"line1\nline2"');
  });

  it('quotes values containing carriage returns', () => {
    const csv = exportAndReadCsv([{ ...baseUser, notes: 'line1\rline2' }]);
    expect(csv).toContain('"line1\rline2"');
  });

  it('renders null and undefined fields as empty cells', () => {
    const csv = exportAndReadCsv([
      { ...baseUser, notes: null, managerEmail: undefined },
    ]);
    const dataRow = csv.split('\r\n')[1];

    expect(dataRow?.endsWith(',')).toBe(true);
    expect(dataRow).not.toContain('null');
    expect(dataRow).not.toContain('undefined');
  });

  it('creates a download link and revokes the object URL', () => {
    exportAndReadCsv([baseUser]);

    expect(document.createElement).toHaveBeenCalledWith('a');
    expect(mockAnchor.href).toBe('blob:mock-url');
    expect(mockAnchor.download).toBe('users-2025-05-22.csv');
    expect(mockAnchor.click).toHaveBeenCalledTimes(1);
    expect(createObjectURL).toHaveBeenCalledTimes(1);
    expect(revokeObjectURL).toHaveBeenCalledWith('blob:mock-url');
  });
});
