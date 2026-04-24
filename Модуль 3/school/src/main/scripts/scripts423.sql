select st.name, st.age, f.name as nameFaculty from student as st
inner join faculty as f on st.faculty_id = f.id;

select st.name, st.age, a.data from student as st
inner join avatar as a on a.student_id = st.id;