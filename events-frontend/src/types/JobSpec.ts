export interface JobSpec {
  cvId: number | null;
  id: number;
  job_spec_content: string;
  location: string | null;
  salary: number | null;
  score: number | null;
  company: string | null;
  jobTitle: string | null;
  status: string | null;
}
