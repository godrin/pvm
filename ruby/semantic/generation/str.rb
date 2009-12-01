class String
  def escape
    self.gsub("\\","\\\\").gsub("\"","\\\"")
  end
end

class StrCode
  def init
    @v=tmpVar
  end

  def before
    s("#{@v}=\"#{@str.escape}\"")
  end

  def value
    @v
  end

  def code
    []
  end

  def after
    s("clear #{@v}")
  end
end