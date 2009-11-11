$i=0
def b
  $i=1
  raise "My Exception"
  $i=2
end
def a
  $i=3
  b
  $i=4
end

j=0
begin
  j=1
  a
  j=2
rescue String=>e
  j=3
  if e=="MyException"
    j=4
  else
    j=5
  end
end

puts $i
puts j
