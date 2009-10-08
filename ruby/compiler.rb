require 'ubygems'
require 'ruby_parser.rb'
require 'pp'

require File.expand_path('../generators.rb',__FILE__)

def assert
  result=yield
  raise 1 unless result
end

class IO
  def self.load(filename)
    File.open(filename) {|f|f.read}
  end
end

class String
  def indent
    "  "+gsub("\n","\n  ")
  end

  def upcaseFirstLetter
    self[0..0].upcase+self[1..-1]
  end
end

class Compiler

  Result=Struct.new(:before,:within,:after)
  def initialize()
    @counter=0
  end

  def tmpVar
    @counter+=1
    "tmp_#{@counter}"
  end

  def compile(input)
    return nil if input.nil?
    args=input.map{|x|x}
    #pp "COMP:",args
    klassName=args[0].to_s.upcaseFirstLetter
    klass=eval(klassName+"Code")
    obj=klass.new(*args[1..-1])
    obj.compiler=self
    result=obj.getCode
    #pp "RESULT #{klassName}:",result
    result
  end
end

@processor = RubyParser.new
c=STDIN.read

a=@processor.parse(c)
c=Compiler.new
#pp a
result=c.compile(a)

#pp result
puts result